/*
 * Copyright 2016 ZBL Services, Inc.
 * Copyright 2015 IBM Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zblservices.doctorbatch.io.patterns;

import java.io.BufferedReader;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.zblservices.doctorbatch.io.Reader;

/**
 * RegexPatternRecordReader uses an regular expression pattern to match record 
 * delimiters in an input source reader.
 * 
 * @author Timothy C. Fanelli (tfanelli@zblservices.com, tim@fanel.li)
 *
 */
public abstract class RegexPatternRecordReader implements Reader<String>{
	private Pattern pattern;
	private Scanner scanner;

	public void setSource( BufferedReader reader ) {
		this.scanner = new Scanner(reader);
	}
	
	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	public void setPattern(String pattern) {
		setPattern(Pattern.compile(pattern));
	}

	@Override
	public String read() {
		scanner.useDelimiter(pattern);
		return ( scanner.hasNext() ? scanner.next() : null );
	}

}
