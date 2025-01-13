package com.cbk.sqlgenerate.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class FreeMarkerConfigurationConfig {

    @Bean
    public Configuration configuration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        // 使用 ClassLoader 加载模板，这样更适合打包后的运行
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        return cfg;
    }
}
