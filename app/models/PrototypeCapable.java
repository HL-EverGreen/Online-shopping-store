package models;

public interface PrototypeCapable extends Cloneable
{
    PrototypeCapable clone() throws CloneNotSupportedException;
}