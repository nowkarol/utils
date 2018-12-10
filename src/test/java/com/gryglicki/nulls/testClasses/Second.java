package com.gryglicki.nulls.testClasses;

public class Second {
  private Third third;
  private ThirdSubclass thirdSubclass;

  public Second(Third third, ThirdSubclass thirdSubclass) {
    this.third = third;
    this.thirdSubclass = thirdSubclass;
  }

  public Second(Third third) {
    this(third, null);
  }

  public Third getThird() {
    return third;
  }

  public ThirdSubclass getThirdSubclass() {
    return thirdSubclass;
  }
}
