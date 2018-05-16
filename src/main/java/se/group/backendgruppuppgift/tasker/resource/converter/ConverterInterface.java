package se.group.backendgruppuppgift.tasker.resource.converter;

public interface ConverterInterface <W, O, E>{

    O fromWebToEntityData(W w);
    O fromEntityToWebData(E e);
}