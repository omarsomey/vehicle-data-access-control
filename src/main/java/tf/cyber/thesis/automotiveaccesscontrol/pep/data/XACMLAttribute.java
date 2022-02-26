package tf.cyber.thesis.automotiveaccesscontrol.pep.data;

import tf.cyber.thesis.automotiveaccesscontrol.pep.mapper.tojava.JavaMapper;
import tf.cyber.thesis.automotiveaccesscontrol.pep.mapper.toxacml.XACMLMapper;

import java.util.Objects;

public class XACMLAttribute<T> {
    private final String id;
    private final String dataType;
    private final T value;

    public XACMLAttribute(String id, T value) {
        this.id = id;
        this.dataType = DataTypes.map(value.getClass());
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getDataType() {
        return dataType;
    }

    public T getValue() {
        return value;
    }

    public String getXACMLValue() {
        return XACMLMapper.map(value);
    }

    public static XACMLAttribute<?> of(String id, String dataType, String value) {
        Class<?> type = DataTypes.map(dataType);
        Object mapped = JavaMapper.map(type, value);

        return new XACMLAttribute<>(id, mapped);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XACMLAttribute<T> that = (XACMLAttribute<T>) o;
        return id.equals(that.id) && dataType.equals(that.dataType) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataType, value);
    }

    @Override
    public String toString() {
        return "XACMLAttribute{" +
                "id='" + id + '\'' +
                ", dataType='" + dataType + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
