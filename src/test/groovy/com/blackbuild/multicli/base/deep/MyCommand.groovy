/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2017 Stephan Pauxberger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
