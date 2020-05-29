---
title: 全文搜索ElasticSearch（一）数据的增删改查
date: 2018-04-16 00:44:28
url: lucene/elasticsearch-base-curd
tags:
- Elasticsearch
categories:
- Elasticsearch
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![Elasticsearch logo](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/3d90604a4a775e8ce0450bac9bc4234c.jpg)

ElasticSearch采用Java编写，提供简单易用的RESTFul API。我们可以直接通过API接口进行操作。
笔者使用的ElasticSearch版本：elasticsearch-6.3.0  

<!-- more -->

API基本格式 http://<ip>:<port>/<索引>/<类型>/<文档id>
常用的HTTP动词 GET/PUT/POST/DELETE
[RESTFul资料](http://www.ruanyifeng.com/blog/2014/05/restful_api)

## 索引创建
### 非结构化创建
使用Head插件直接点击添加索引创建。
直接请求创建 $ curl -XPUT http://localhost:9200/book
​	
### 结构化创建

方式1：首先创建book索引
```
PUT http://localhost:9200/book。
```

再添加novel类型以及定义结构。
```json
POST http://localhost:9200/book/novel/_mappings
{
  "novel": {
    "properties": {
      "title": {
        "type": "text"
      }
    }
  }
}
```

方式2：直接创建people结构化索引指定配置信息已经数据结构。
```json
// 创建
PUT http://localhost:9200/people
{
	"settings":{
		"number_of_shards": 3,
		"number_of_replicas": 1
	},
	"mappings":{
		"man":{
			"properties":{
				"name":{
					"type":"text"
				},
				"country":{
					"type":"keyword"
				},
				"age":{
					"type":"integer"
				},
				"date":{
					"type":"date",
					"format":"yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis"
				}
			}
		},
		"woman":{
			
		}
	}
}

// 响应
{
    "acknowledged": true,
    "shards_acknowledged": true
}

```

### 查看索引信息

查询一个索引是否为结构化索引，我们只需要查询索引信息，然后查看mappings属性是否有值，如果为空，则为非结构化索引，如果有相关的属性值，则是结构化索引。下面查看book索引信息：
```json
GET http://localhost:9200/book?pretty=true
{
    "book": {
        "aliases": {},
        "mappings": {
            "novel": {
                "properties": {
                    "title": {
                        "type": "text"
                    }
                }
            }
        },
        "settings": {
            "index": {
                "creation_date": "1523851142335",
                "number_of_shards": "5",
                "number_of_replicas": "1",
                "uuid": "nbQPVXqqR--eRy_1S3OZqw",
                "version": {
                    "created": "5050199"
                },
                "provided_name": "book"
            }
        }
    }
}
```

## 数据插入

### 根据文档ID插入
直接POST请求 http://<ip>:<port>/<索引>/<类型>/<文档id>
传入JSON数据就可以完成数据插入：
```json
POST http://localhost:9200/people/man/1
{
	"name":"达西",
	"conuntry":"China",
	"age":24,
	"date":"1994-11-01"
}

// 响应
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "created": true
}
```

### 自动生成文档ID插入
在发起POST请求的时候，不指定ID的值，ES会自动生成一个ID值。
```json
POST http://localhost:9200/people/man
{
	"name":"超级玛丽",
	"conuntry":"China",
	"age":24,
	"date":"1994-11-01"
}

// 响应
{
    "_index": "people",
    "_type": "man",
    "_id": "AWLNIi3-QcKhhHA2xN6t", // 自动生成的ID值
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "created": true
}
```

## 数据修改

### 直接修改
修改people索引中类型为man的ID为1的文档的name字段值为“曹操”
```json
POST http://localhost:9200/people/man/1/_update
{
	"doc":{
		"name":"曹操"
	}
}

// 响应
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 5,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    }
}
```

### 脚本修改
修改people索引中类型为man的ID为1的文档的age字段值为自增10。
```json
POST http://localhost:9200/people/man/1/_update
{
	"script":{
		"lang":"painless",
		"inline":"ctx._source.age+=10"
	}
}

// 响应
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 9,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    }
}
```
另外一种方式
```json
POST http://localhost:9200/people/man/1/_update
{
	"script":{
		"lang":"painless",
		"inline":"ctx._source.age=params.age",
		"params":{
			"age":900
		}
	}
}
// 响应
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 10,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    }
}
```

## 删除

### 文档删除
删除people索引中类型为man，ID为1的文档。
```json
DELETE http://localhost:9200/people/man/1

// 响应
{
    "found": true,
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 11,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    }
}
```

### 索引删除
删除people索引
```json
DELETE http://localhost:9200/people

// 响应
{
    "acknowledged": true
}
```

## 基本查询
### 数据准备
创建book索引，设置结构：

```json
POST http://localhost:9200/book/novel/_mappings
{
	"novel":{
		"properties":{
			"title":{
				"type":"text"
			},
			"author":{
				"type":"text"
			},
			"word_count":{
				"type":"integer"
			},
			"publish_date":{
				"type":"date",
				"format":"yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis"
			}
		}
	}
}

// 响应
{
    "acknowledged": true
}

```
录入用于测试的数据：
```json
{"title":"超级玛丽秘籍","author":"王五","word_count":5000,"publish_date":"1994-11-01"}
{"title":"三国志","author":"陈寿","word_count":30000,"publish_date":"1995-11-01"}
{"title":"三国演绎","author":"罗贯中","word_count":30000,"publish_date":"1994-03-01"}
{"title":"玛丽与管道工","author":"玛丽","word_count":10000,"publish_date":"1994-03-01"}
{"title":"史记","author":"司马迁","word_count":20000,"publish_date":"1994-08-01"}
{"title":"如何蹦的更高","author":"玛丽","word_count":20000,"publish_date":"1994-08-01"}
```
## 查询
### 全部查询
查询book索引中类型为novel的所有文档信息。
```json
GET localhost:9200/book/novel/_search?pretty=true

// 响应
{
  "took" : 80,  // 花费时间
  "timed_out" : false,
  "_shards" : {
    "total" : 5, 
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 5,  //总条数
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "book",
        "_type" : "novel",
        "_id" : "AWLOAAZzQcKhhHA2xN6z",
        "_score" : 1.0,  //评分
        "_source" : {
          "title" : "超级玛丽秘籍",
          "author" : "王五",
          "word_count" : 5000,
          "publish_date" : "1994-11-01"
        }
      },
      {
        "_index" : "book",
        "_type" : "novel",
        "_id" : "AWLOAEe5QcKhhHA2xN62",
        "_score" : 1.0,
        "_source" : {
          "title" : "玛丽与管道工",
          "author" : "陈寿",
          "word_count" : 10000,
          "publish_date" : "1994-03-01"
        }
      },
      {
        "_index" : "book",
        "_type" : "novel",
        "_id" : "AWLOAB8bQcKhhHA2xN60",
        "_score" : 1.0,
        "_source" : {
          "title" : "三国志",
          "author" : "陈寿",
          "word_count" : 30000,
          "publish_date" : "1995-11-01"
        }
      },
      {
        "_index" : "book",
        "_type" : "novel",
        "_id" : "AWLOADU4QcKhhHA2xN61",
        "_score" : 1.0,
        "_source" : {
          "title" : "三国演绎",
          "author" : "罗贯中",
          "word_count" : 30000,
          "publish_date" : "1994-03-01"
        }
      },
      {
        "_index" : "book",
        "_type" : "novel",
        "_id" : "AWLOAFiZQcKhhHA2xN63",
        "_score" : 1.0,
        "_source" : {
          "title" : "史记",
          "author" : "司马迁",
          "word_count" : 20000,
          "publish_date" : "1994-08-01"
        }
      }
    ]
  }
}

```

### 精确查询
查询book索引中类型为novel的ID为AWLOAFiZQcKhhHA2xN63的文档信息。
```json
GET localhost:9200/book/novel/AWLOAFiZQcKhhHA2xN63?pretty=true
// 响应
{
  "_index" : "book",
  "_type" : "novel",
  "_id" : "AWLOAFiZQcKhhHA2xN63",
  "_version" : 1,
  "found" : true,
  "_source" : {
    "title" : "史记",
    "author" : "司马迁",
    "word_count" : 20000,
    "publish_date" : "1994-08-01"
  }
}

```


### 限制查询条数
搜索从第一条开始的1条数据。
```json
POST http://localhost:9200/book/novel/_search
{
	"query":{
		"match_all":{}
	},
	"from":1,
	"size":1
}

// 响应
{
    "took": 37,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "failed": 0
    },
    "hits": {
        "total": 5,
        "max_score": 1,
        "hits": [
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAEe5QcKhhHA2xN62",
                "_score": 1,
                "_source": {
                    "title": "玛丽与管道工",
                    "author": "陈寿",
                    "word_count": 10000,
                    "publish_date": "1994-03-01"
                }
            }
        ]
    }
}
```

### 关键词排序查询
查询标题里含有“三国”的书籍，并且按出版日期降序排列。

```json
POST http://localhost:9200/book/novel/_search
{
	"query":{
		"match":{
			"title":"三国"
		}
	},
	"sort":[
		{
			"publish_date":{"order":"desc"}
		}
	]
}

// 响应
{
    "took": 200,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "failed": 0
    },
    "hits": {
        "total": 2,
        "max_score": 0.34450945,
        "hits": [
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAB8bQcKhhHA2xN60",
                "_score": 0.34450945,
                "_source": {
                    "title": "三国志",
                    "author": "陈寿",
                    "word_count": 30000,
                    "publish_date": "1995-11-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOADU4QcKhhHA2xN61",
                "_score": 0.34450945,
                "_source": {
                    "title": "三国演绎",
                    "author": "罗贯中",
                    "word_count": 30000,
                    "publish_date": "1994-03-01"
                }
            }
        ]
    }
}
```

### 聚合查询
类似于数据库中的分组查询，下面的例子演示按照书籍字数统计数据，按照发布时间统计数量。
```json
POST http://localhost:9200/book/novel/_search
{
	"aggs":{
		"group_by_word_count":{
			"terms":{
				"field":"word_count"
			}
		},
		"group_by_author":{
			"terms":{
				"field":"publish_date"
			}
		}
	}
	
}
// 响应
{
    "took": 4,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "failed": 0
    },
    "hits": {
        "total": 5,
        "max_score": 1,
        "hits": [
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAAZzQcKhhHA2xN6z",
                "_score": 1,
                "_source": {
                    "title": "超级玛丽秘籍",
                    "author": "王五",
                    "word_count": 5000,
                    "publish_date": "1994-11-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAEe5QcKhhHA2xN62",
                "_score": 1,
                "_source": {
                    "title": "玛丽与管道工",
                    "author": "陈寿",
                    "word_count": 10000,
                    "publish_date": "1994-03-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAB8bQcKhhHA2xN60",
                "_score": 1,
                "_source": {
                    "title": "三国志",
                    "author": "陈寿",
                    "word_count": 30000,
                    "publish_date": "1995-11-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOADU4QcKhhHA2xN61",
                "_score": 1,
                "_source": {
                    "title": "三国演绎",
                    "author": "罗贯中",
                    "word_count": 30000,
                    "publish_date": "1994-03-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAFiZQcKhhHA2xN63",
                "_score": 1,
                "_source": {
                    "title": "史记",
                    "author": "司马迁",
                    "word_count": 20000,
                    "publish_date": "1994-08-01"
                }
            }
        ]
    },
    "aggregations": {
        "group_by_word_count": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0,
            "buckets": [
                {
                    "key": 30000,
                    "doc_count": 2
                },
                {
                    "key": 5000,
                    "doc_count": 1
                },
                {
                    "key": 10000,
                    "doc_count": 1
                },
                {
                    "key": 20000,
                    "doc_count": 1
                }
            ]
        },
        "group_by_author": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0,
            "buckets": [
                {
                    "key": 762480000000,
                    "key_as_string": "1994-03-01 00:00:00",
                    "doc_count": 2
                },
                {
                    "key": 775699200000,
                    "key_as_string": "1994-08-01 00:00:00",
                    "doc_count": 1
                },
                {
                    "key": 783648000000,
                    "key_as_string": "1994-11-01 00:00:00",
                    "doc_count": 1
                },
                {
                    "key": 815184000000,
                    "key_as_string": "1995-11-01 00:00:00",
                    "doc_count": 1
                }
            ]
        }
    }
}

```

### 统计查询
聚合查询也可以用于查询统计信息，下面的例子演示统计书籍字数信息，统计书籍字数最小的字数信息。
```json
POST http://localhost:9200/book/novel/_search
{
	"aggs":{
		"grades_word_count":{
			"stats":{
				"field":"word_count"
			}
		},
		"min_word_count":{
			"min":{
				"field":"word_count"
			}
		}
	}
	
}
// 响应
{
    "took": 44,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "failed": 0
    },
    "hits": {
        "total": 5,
        "max_score": 1,
        "hits": [
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAAZzQcKhhHA2xN6z",
                "_score": 1,
                "_source": {
                    "title": "超级玛丽秘籍",
                    "author": "王五",
                    "word_count": 5000,
                    "publish_date": "1994-11-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAEe5QcKhhHA2xN62",
                "_score": 1,
                "_source": {
                    "title": "玛丽与管道工",
                    "author": "陈寿",
                    "word_count": 10000,
                    "publish_date": "1994-03-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAB8bQcKhhHA2xN60",
                "_score": 1,
                "_source": {
                    "title": "三国志",
                    "author": "陈寿",
                    "word_count": 30000,
                    "publish_date": "1995-11-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOADU4QcKhhHA2xN61",
                "_score": 1,
                "_source": {
                    "title": "三国演绎",
                    "author": "罗贯中",
                    "word_count": 30000,
                    "publish_date": "1994-03-01"
                }
            },
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAFiZQcKhhHA2xN63",
                "_score": 1,
                "_source": {
                    "title": "史记",
                    "author": "司马迁",
                    "word_count": 20000,
                    "publish_date": "1994-08-01"
                }
            }
        ]
    },
    "aggregations": {
        "grades_word_count": {
            "count": 5,
            "min": 5000,
            "max": 30000,
            "avg": 19000,
            "sum": 95000
        },
        "min_word_count": {
            "value": 5000
        }
    }
}
```


## 参考资料
[Elasticsearch Reference](https://www.elastic.co/guide/en/elasticsearch/reference/5.5/index.html)
[Elasticsearch: 权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html)  (低版本)

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)