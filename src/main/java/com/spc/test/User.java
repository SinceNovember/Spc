package com.spc.test;

import com.spc.annotation.Param;
import com.spc.annotation.SpcClient;
import com.spc.annotation.Sql;
import com.spc.config.DatasourceConfig;

import java.util.List;

@SpcClient(name = "user")
public interface User {

    @Sql("select * from frame_task_msg where categoryguid =#{categoryguid}")
    List<String> findNameById(@Param("categoryguid")String categoryguid);

}