/*
 * Copyright (c) 2020, Red Hat, Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

/*
 * @test TestModeUnlock
 * @summary Test that Shenandoah modes are unlocked properly
 * @key gc
 * @library /testlibrary
 * @run driver TestModeUnlock
 */

import com.oracle.java.testlibrary.*;

public class TestModeUnlock {

    enum Mode {
        PRODUCT,
        DIAGNOSTIC,
        EXPERIMENTAL,
    }

    public static void main(String[] args) throws Exception {
        testWith("-XX:ShenandoahGCMode=normal",  Mode.PRODUCT);
        testWith("-XX:ShenandoahGCMode=iu",      Mode.EXPERIMENTAL);
        testWith("-XX:ShenandoahGCMode=passive", Mode.DIAGNOSTIC);
    }

    private static void testWith(String h, Mode mode) throws Exception {
        if (false) { // When ShenandoahGC is experimental flag, this makes no sense to test
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder(
                    "-XX:-UnlockDiagnosticVMOptions",
                    "-XX:-UnlockExperimentalVMOptions",
                    "-XX:+UseShenandoahGC",
                    h,
                    "-version"
            );
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            switch (mode) {
                case PRODUCT:
                    output.shouldHaveExitValue(0);
                    break;
                case DIAGNOSTIC:
                case EXPERIMENTAL:
                    output.shouldHaveExitValue(1);
                    break;
            }
        }

        if (false) { // When ShenandoahGC is experimental flag, this makes no sense to test
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder(
                    "-XX:+UnlockDiagnosticVMOptions",
                    "-XX:-UnlockExperimentalVMOptions",
                    "-XX:+UseShenandoahGC",
                    h,
                    "-version"
            );
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            switch (mode) {
                case PRODUCT:
                case DIAGNOSTIC:
                    output.shouldHaveExitValue(0);
                    break;
                case EXPERIMENTAL:
                    output.shouldHaveExitValue(1);
                    break;
            }
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder(
                    "-XX:-UnlockDiagnosticVMOptions",
                    "-XX:+UnlockExperimentalVMOptions",
                    "-XX:+UseShenandoahGC",
                    h,
                    "-version"
            );
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            switch (mode) {
                case PRODUCT:
                case EXPERIMENTAL:
                    output.shouldHaveExitValue(0);
                    break;
                case DIAGNOSTIC:
                    output.shouldHaveExitValue(1);
                    break;
            }
        }
    }

}
