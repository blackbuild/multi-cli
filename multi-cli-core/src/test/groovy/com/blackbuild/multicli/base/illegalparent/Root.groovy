package com.blackbuild.multicli.base.illegalparent

import com.blackbuild.multicli.base.ParentAware
import com.blackbuild.multicli.base.SubCommandOf
import picocli.CommandLine

@CommandLine.Command
class Root {
}

@SubCommandOf(Root)
@CommandLine.Command(name = "sub1")
class Sub1 {
}

@SubCommandOf(Root)
@CommandLine.Command(name = "sub2")
class Sub2 implements ParentAware<Sub1> {
    private Sub1 parent

    @Override
    void setParent(Sub1 parent) {
        this.parent = parent
    }
}

@SubCommandOf(Sub1)
@CommandLine.Command(name = "subsub1")
class SubSub1 {
}


