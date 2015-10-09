package com.assis.redondo.daniel.maptest.dispatcher;

public interface Event {

    public String getType();

    public Object getSource();

    public void setSource(Object source);
}
