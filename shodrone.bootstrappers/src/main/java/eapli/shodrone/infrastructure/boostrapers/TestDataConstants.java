/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eapli.shodrone.infrastructure.boostrapers;

import eapli.framework.time.util.Calendars;

import java.util.Calendar;

/**
 * @author Paulo Gandra de Sousa 13/04/2020
 */
public final class TestDataConstants {

    public static final String USER_TEST1 = "user1";
    public static final String USER_TEST2 = "isep959";

    @SuppressWarnings("squid:S2068")
    public static final String PASSWORD1 = "123Qwe#";

    public static final String USER_PHONE_NUMBER = "+351912345678";

    // this needs to be a date in the future
    @SuppressWarnings("squid:S2885")
    public static final Calendar DATE_TO_BOOK = Calendars.of(2080, 3, 31);

    private TestDataConstants() {
        // ensure utility
    }
}
