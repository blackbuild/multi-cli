package com.blackbuild.multicli.base.base

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
class Sub2 {
}

@SubCommandOf(Sub1)
@CommandLine.Command(name = "subsub1")
class SubSub1 {
}


