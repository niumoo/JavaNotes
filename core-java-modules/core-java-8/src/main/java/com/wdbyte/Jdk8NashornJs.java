package com.wdbyte;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.jupiter.api.Test;

/**
 * <p>
 *
 * Java 8提供了新的Nashorn JavaScript引擎，使得我们可以在JVM上开发和运行JS应用。
 * 
 * Nashorn JavaScript引擎是javax.script.ScriptEngine的另一个实现版本，
 * 
 * 这类Script引擎遵循相同的规则，允许Java和JavaScript交互使用，例子代码如下：
 *
*  @Author https://www.wdbyte.com
 * @Date 2019/6/12 9:41
 */
public class Jdk8NashornJs {

    @Test
    public void nashornJsTest() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine jsEngine = scriptEngineManager.getEngineByName("JavaScript");
        System.out.println(jsEngine.getClass().getName());
        String string = (String)jsEngine.eval("var str=\"hello\";str+\" java\"");
        System.out.println(string);

        // result
        // jdk.nashorn.api.scripting.NashornScriptEngine
        // hello java
    }

}
