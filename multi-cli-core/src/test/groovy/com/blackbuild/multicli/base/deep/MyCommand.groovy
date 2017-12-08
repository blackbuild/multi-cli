package com.blackbuild.multicli.base.deep

import com.blackbuild.multicli.base.ParentAware
import com.blackbuild.multicli.base.SubCommandOf
import picocli.CommandLine

class MyCommand {

    enum LogLevel { info, debug, quiet }

    @CommandLine.Option(names = ['-l', '--loglevel'])
    LogLevel logLevel
}

@SubCommandOf(MyCommand)
@CommandLine.Command(name='info')
class Info implements ParentAware<MyCommand> {

    private MyCommand parent

    @CommandLine.Option(names = ['-v', '--values'])
    List<String> values

    @Override
    void setParent(MyCommand parent) {
        this.parent = parent
    }


}
