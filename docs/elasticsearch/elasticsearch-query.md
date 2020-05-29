---
title: 全文搜索ElasticSearch（二）深入搜索
date: 2018-04-27 15:14:17
url: lucene/elasticsearch-query
tags:
- Elasticsearch
categories:
- Elasticsearch
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![全文搜索](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/534fa087bb57ff07a93d9766d1a1b8fa.jpg)
现在，我们已经学会了如何使用 Elasticsearch 作为一个简单的 NoSQL 风格的分布式文档存储系统。我们可以将一个 JSON 文档扔到 Elasticsearch 里，然后根据 ID 检索。但 Elasticsearch 真正强大之处在于可以从无规律的数据中找出有意义的信息——从“大数据”到“大信息”。

Elasticsearch 不只会存储（stores） 文档，为了能被搜索到也会为文档添加索引（indexes） ，这也是为什么我们使用结构化的 JSON 文档，而不是无结构的二进制数据。

文档中的每个字段都将被索引并且可以被查询 。不仅如此，在简单查询时，Elasticsearch 可以使用 所有（all） 这些索引字段，以惊人的速度返回结果。这是你永远不会考虑用传统数据库去做的一些事情。
<!-- more -->

笔者使用的ElasticSearch版本：**elasticsearch-5.5.1**
## 数据准备
```json
{"title":"超级玛丽秘籍","author":"王五","word_count":5000,"publish_date":"1994-11-01"}
{"title":"三国志","author":"陈寿","word_count":30000,"publish_date":"1995-11-01"}
{"title":"三国演绎","author":"罗贯中","word_count":30000,"publish_date":"1994-03-01"}
{"title":"玛丽与管道工","author":"玛丽","word_count":10000,"publish_date":"1994-03-01"}
{"title":"史记","author":"司马迁","word_count":20000,"publish_date":"1994-08-01"}
{"title":"如何蹦的更高","author":"玛丽","word_count":20000,"publish_date":"1994-08-01"}
```

## 子条件查询

### Query context
在查询过程中，除了判断文档是否满足查询条件外，ES还会计算一个_score来标识匹配的程度，旨在判断目标文档和查询条件匹配的**有多好**。

常用查询：
全文本查询，针对**文本类型数据**

#### 全文本查询-模糊查询

```json
POST http://localhost:9200/book/_search
{
	"query":{
		"match":{
			"title":"三国志"
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
        "total": 2,
        "max_score": 0.99938464,
        "hits": [
            {
                "_index": "book",
                "_type": "novel",
                "_id": "AWLOAB8bQcKhhHA2xN60",
                "_score": 0.99938464,
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

#### 全文本查询-短语查询
查询书籍名字就是”三国志”的书籍信息。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"match_phrase":{
			"title":"三国志"
		}
	}
}

```

#### 全文本查询-多字段查询
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"multi_match":{
			"query":"玛丽",
			"fields":["author","title"]
		}
	}
}
```
#### 全文本查询-语法查询
查询“三国”和”演绎”同时匹配的书籍信息或者可以匹配“超级玛丽”的书籍信息。

```json
POST http://localhost:9200/book/_search
{
	"query":{
		"query_string":{
			"query":"(三国 AND 演绎) OR 超级玛丽"
		}
	}
}
```

指定字段的语法查询，查询标题中包含三国或者玛丽的书籍信息。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"query_string":{
			"query":"三国 OR 玛丽",
			"fields":["title"]
		}
	}
}
```

字段级别查询，针对结构化数据，如数字，日期等
#### 字段级别查询-精确查询
查询书籍字数为10000的书籍信息。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"term":{
			"word_count":10000
		}
	}
}
```

#### 字段级别查询-范围查询
查询书籍字数大于等于10000且小于等于20000的书籍信息，gte中的e是equals，等于的意思。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"range":{
			"word_count":{
				"gte":10000,
				"lte":20000
			}	
		}
	}
}
```


### Filter context
在查询的过程中，只判断该文档是否满足条件，只有YES或者NO的结果。
使用Filter查询书籍字数为10000的书籍信息。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"bool":{
			"filter":{
				"term":{
					"word_count":10000
				}
			}
		}
	}
}
```

## 复合条件查询

### 固定分数查询
"boost":2用于指定查询出来的信息评分都为2。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"constant_score":{
			"filter":{
				"match":{
					"title":"三国"
				}
			},
			"boost":2
		}
	}
}
```
### 组合或关系查询
查询作者是陈寿或者标题为三国志的书籍信息。
should表示条件之间是或的关系。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"bool":{
			"should":[
			  {
				"match":{
					"author":"陈寿"
				}
			  },
			  {
			  	"match":{
			  		"title":"三国志"
			  	}
			  }
			]
		}
	}
}
```
### 组合与关系查询
查询作者是陈寿或者标题为三国志的书籍信息。
Must表示条件之间是且的关系。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"bool":{
			"must":[
			  {
				"match":{
					"author":"陈寿"
				}
			  },
			  {
			  	"match":{
			  		"title":"三国志"
			  	}
			  }
			]
		}
	}
}
```

### 组合非关系查询
查询作者不是陈寿的书籍信息。
```json
POST http://localhost:9200/book/_search
{
	"query":{
		"bool":{
			"must_not":{
				"match":{
					"author":"陈寿"
			  }
			}
		}
	}
}
```

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)