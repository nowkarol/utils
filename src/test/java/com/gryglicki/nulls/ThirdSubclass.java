package com.gryglicki.nulls;

class ThirdSubclass extends Third {
  private Last last;

  ThirdSubclass(Last last) {
    super(last);
    this.last = last;
  }

  Last getLast() {
    return last;
  }
}
