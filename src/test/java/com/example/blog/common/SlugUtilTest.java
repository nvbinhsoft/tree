package com.example.blog.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlugUtilTest {

    @Test
    void convertsSimplePhrase() {
        assertEquals("hello-world", SlugUtil.toSlug("Hello, World!"));
    }

    @Test
    void stripsAccentsAndCollapsesSeparators() {
        assertEquals("cafe-au-lait", SlugUtil.toSlug("Café   au\tlait"));
    }

    @Test
    void fallsBackWhenResultIsEmpty() {
        assertEquals("post", SlugUtil.toSlug("你好"));
        assertEquals("post", SlugUtil.toSlug(null));
    }
}
