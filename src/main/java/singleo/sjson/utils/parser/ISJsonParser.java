package singleo.sjson.utils.parser;

import singleo.sjson.entity.SJsonType;

public interface ISJsonParser {
    SJsonType getObjectType(Object object);
}
