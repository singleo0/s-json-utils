package singleo.sjson.utils.parser;

public interface IJsonParser {
    Object parse(String text);

    Object toJSON(Object object);
}
