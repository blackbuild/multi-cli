package com.blackbuild.multicli.base.base

import com.blackbuild.multicli.base.MockCommand
import com.blackbuild.multicli.base.MultiCommand
import picocli.CommandLine

@CommandLine.Command
class Root extends MockCommand {
}

@MultiCommand(Root)
@CommandLine.Command(name = "sub1")
class Sub1 extends MockCommand {
}

@MultiCommand(Root)
@CommandLine.Command(name = "sub2")
class Sub2 extends MockCommand {
}

@MultiCommand(Sub1)
@CommandLine.Command(name = "subsub1")
class SubSub1 extends MockCommand {
}


