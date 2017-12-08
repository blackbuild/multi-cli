package com.blackbuild.multicli.base.parentaware

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
class Sub2 implements ParentAware<Root> {
    private Root parent

    @Override
    void setParent(Root parent) {
        this.parent = parent
    }
}

@SubCommandOf(Sub1)
@CommandLine.Command(name = "subsub1")
class SubSub1 {
}


