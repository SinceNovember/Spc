package com.spc.test;

import com.spc.annotation.Param;
import com.spc.annotation.SpcClient;
import com.spc.annotation.Sql;
import com.spc.config.DatasourceConfig;

import javax.xml.crypto.Data;
import java.util.List;

@SpcClient(name = "mytest", datasourceConfig = DatasourceConfig.class)
public interface MyTest {

    @Sql("select * from frame_task_msg where rowguid=#{rowguid}")
    List<String> findUserById(@Param("rowguid") String rowguid);

}
