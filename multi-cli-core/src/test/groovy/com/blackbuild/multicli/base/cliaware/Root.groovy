package com.blackbuild.multicli.base.cliaware

import com.blackbuild.multicli.base.CommandLineAware
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
class Sub2 implements CommandLineAware {
    private CommandLine commandLine

    @Override
    void setCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine
    }
}

@SubCommandOf(Sub1)
@CommandLine.Command(name = "subsub1")
class SubSub1 {
}


