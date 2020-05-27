package com.bupo.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.ahocorasick.trie.Emit;
import org.junit.Assert;
import org.junit.Test;

public class StringReaderServiceTest {

	@Test
	public void searchStrTrueTest() {
		StringReaderService stringReaderService = new StringReaderService();
		String inputString = "A for Apple, B for Boy";
		Set<String> words = new HashSet<>(Arrays.asList("Apple", "Boy"));

		Collection<Emit> resultsCollection = stringReaderService.findSubStrings(inputString, words);
		System.out.println(resultsCollection);
		Assert.assertNotSame("Sub Search failed", 0, resultsCollection.size());

	}

	@Test
	public void searchStrFalseTest() {
		StringReaderService stringReaderService = new StringReaderService();
		String inputString = "A for Apple, B for Boy";
		Set<String> words = new HashSet<>(Arrays.asList("Appleasdf", "Boyasdf"));

		Collection<Emit> resultsCollection = stringReaderService.findSubStrings(inputString, words);

		System.out.println("searchStrFalseTest" + resultsCollection);
		Assert.assertSame("Sub Search failed, it found something when it shouldn't have", 0, resultsCollection.size());

	}

}
