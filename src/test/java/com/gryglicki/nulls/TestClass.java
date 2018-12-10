package com.gryglicki.nulls;


public class TestClass
{
    public TestClass reference;

    public TestClass(TestClass reference)
    {
        this.reference = reference;
    }

    public TestClass getReference()
    {
        return reference;
    }
}
