package com.blackbuild.multicli.base.base

import com.blackbuild.multicli.base.EmptyCommand
import com.blackbuild.multicli.base.MultiCommand
import picocli.CommandLine

@CommandLine.Command
class Root extends EmptyCommand {
}

@MultiCommand(Root)
@CommandLine.Command(name = "sub1")
class Sub1 extends EmptyCommand {

}


