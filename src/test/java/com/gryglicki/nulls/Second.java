package com.gryglicki.nulls;

class Second {
  private Third third;

  Second(Third third) {
    this.third = third;
  }

  Third getThird() {
    return third;
  }
}
