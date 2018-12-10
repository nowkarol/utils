package com.gryglicki.nulls.testClasses;

import com.gryglicki.nulls.Last;

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
