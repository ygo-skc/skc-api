package com.rtomyj.yugiohAPI.helper.exceptions.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Slf4j
public class Mapper {

    private final Map<String, MapperDirectives> mapper;

    private final String INHERIT_VALUE_CONFIG_MARKER = "inherit";


    public Mapper(@Autowired final ExceptionMappingConfig mapperConfig) {
        this.mapper = mapperConfig.getMapper();
    }

    public Exception convertException(final Exception fromException, final Class<? extends Exception> fromExceptionClazz) {
        final String fromExceptionClazzName = fromExceptionClazz.getName();
        log.debug(fromExceptionClazzName);
        log.debug("{}", mapper.get(fromExceptionClazzName));

        final MapperDirectives directives = mapper.get(fromExceptionClazzName);
        if (directives == null) {
            log.error("Cannot find mapping strategy for {}", fromExceptionClazzName);
            throw new RuntimeException("Internal Error");
        }
        final String toClass = directives.getTo();
        String message = directives.getMessage();
        String code = directives.getCode();

        Exception toObject = null;


        try {
            final Class<Exception> toClazz = (Class<Exception>) Class.forName(toClass);
            toObject = toClazz.getDeclaredConstructor().newInstance();

            message = (message.equals(INHERIT_VALUE_CONFIG_MARKER))? invokeExceptionGetter("getMessage", fromException) : message;
            code = (code.equals(INHERIT_VALUE_CONFIG_MARKER))? invokeExceptionGetter("getCode", fromException) : code;

            invokeExceptionSetter("setMessage", toObject, message, fromException.getMessage());
            invokeExceptionSetter("setCode", toObject, code, "CODE FROM PREVIOUS EXCEPTION");

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("Couldn't create exception object for toException. Exception message: {}", e.getMessage());
            throw new RuntimeException("Internal Error");
        }

        return toObject;
    }


    private String invokeExceptionGetter(final String methodName, final Exception objectToInvokeOn) {
        try {
            final Method methodToInvoke = objectToInvokeOn.getClass().getMethod(methodName);
            return (String) methodToInvoke.invoke(objectToInvokeOn);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Couldn't invoke getter method: {} on exception class: {}. Does this exception class have the getter method/field? Exception message: {}", methodName, objectToInvokeOn.getClass().getName(), e.getMessage());
            throw new RuntimeException("Internal Error");
        }
    }


    private void invokeExceptionSetter(final String methodName, final Exception objectToInvokeOn, final String valueFromConfig, final String valueFromPreviousException) {
        try {
            final Method methodToInvoke = objectToInvokeOn.getClass().getMethod(methodName, String.class);
            if (valueFromConfig.equals(INHERIT_VALUE_CONFIG_MARKER))
                methodToInvoke.invoke(objectToInvokeOn, valueFromPreviousException);
            else
                methodToInvoke.invoke(objectToInvokeOn, valueFromConfig);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Couldn't invoke setter method: {} on object: {}. Exception message: {}", methodName, objectToInvokeOn, e.getMessage());
            throw new RuntimeException("Internal Error");
        }
    }
}
