package com.spc.handler;

import com.spc.core.SpcMethodMetadata;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface ResultHandler {

    <E>  List<E> handlerResult(Statement statement) throws SQLException;

     interface ResultHandlerFactory{

         ResultHandler create(SpcMethodMetadata methodMetadata);

    }
}
