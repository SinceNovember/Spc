package com.spc.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class SqlExecuteEvent extends ApplicationEvent {

    private SqlExecuteRecord record;

    public SqlExecuteEvent(Object source, SqlExecuteRecord record) {
        super(source);
        this.record = record;
    }
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public SqlExecuteEvent(Object source) {
        super(source);
    }

    public SqlExecuteRecord getRecord() {
        return record;
    }

    public void setRecord(SqlExecuteRecord record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "SqlExecuteEvent{" +
                "record=" + record +
                '}';
    }

    public static class SqlExecuteRecord {

        private long executeTime;

        private List<Object> result;

        public SqlExecuteRecord(long executeTime, List<Object> result) {
            this.executeTime = executeTime;
            this.result = result;
        }

        public long getExecuteTime() {
            return executeTime;
        }

        public void setExecuteTime(long executeTime) {
            this.executeTime = executeTime;
        }

        @Override
        public String toString() {
            return "SqlExecuteRecord{" +
                    "executeTime=" + executeTime +
                    ", result=" + result +
                    '}';
        }
    }
}
