package com.gryglicki.nulls.testClasses;

public class ThirdSubclass extends Third {
  private Last last;

  public ThirdSubclass(Last last) {
    super(last);
    this.last = last;
  }

  public Last getLast() {
    return last;
  }
}
