package com.bupo.services;

import java.util.Collection;
import java.util.Set;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import com.google.common.base.Preconditions;

public class StringReaderService {

	public Collection<Emit> findSubStrings(String inputString, Set<String> words) {
		Preconditions.checkNotNull(inputString, "Input String can't be null");
		Preconditions.checkNotNull(words, "Search Strings can't be null");

		Trie trie = Trie.builder().onlyWholeWords().addKeywords(words).build();

		return trie.parseText(inputString);
	}

}
