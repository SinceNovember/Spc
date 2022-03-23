package com.spc.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SqlExecuteListener implements ApplicationListener<SqlExecuteEvent> {
    @Override
    public void onApplicationEvent(SqlExecuteEvent event) {
        System.out.println(event);
    }
}
