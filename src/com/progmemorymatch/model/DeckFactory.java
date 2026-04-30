package com.progmemorymatch.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class DeckFactory {
    private static final Random RANDOM = new Random();
    private static final List<LanguageInfo> LANGUAGE_LIBRARY = List.of(
        new LanguageInfo(
            "Java",
            "JV",
            "Java is a class-based, object-oriented language focused on portability.",
            "Used for enterprise systems, Android apps, and backend APIs.",
            new Color(238, 112, 46)
        ),
        new LanguageInfo(
            "Python",
            "PY",
            "Python is a high-level language known for simple and readable syntax.",
            "Used in AI/ML, automation, scripting, and backend services.",
            new Color(83, 158, 211)
        ),
        new LanguageInfo(
            "JavaScript",
            "JS",
            "JavaScript powers interactive behavior in modern web applications.",
            "Used for browser apps, Node.js services, and full-stack development.",
            new Color(242, 197, 65)
        ),
        new LanguageInfo(
            "C++",
            "C+",
            "C++ is a high-performance language with low-level memory control.",
            "Used in game engines, real-time systems, and performance-critical apps.",
            new Color(100, 149, 237)
        ),
        new LanguageInfo(
            "C#",
            "C#",
            "C# is a modern object-oriented language in the .NET ecosystem.",
            "Used in desktop apps, web backends, and Unity game development.",
            new Color(122, 64, 181)
        ),
        new LanguageInfo(
            "Go",
            "GO",
            "Go is a compiled language designed for simplicity and concurrency.",
            "Used in cloud infrastructure, microservices, and DevOps tooling.",
            new Color(43, 180, 194)
        ),
        new LanguageInfo(
            "Rust",
            "RS",
            "Rust provides memory safety and speed without a garbage collector.",
            "Used in systems software, secure tools, and performance services.",
            new Color(186, 112, 75)
        ),
        new LanguageInfo(
            "Kotlin",
            "KT",
            "Kotlin is a concise language fully interoperable with Java.",
            "Used in Android apps, JVM backends, and multiplatform projects.",
            new Color(141, 74, 211)
        ),
        new LanguageInfo(
            "Swift",
            "SW",
            "Swift is a modern, safe language for Apple ecosystem development.",
            "Used in iOS, macOS, watchOS, and tvOS applications.",
            new Color(249, 120, 56)
        ),
        new LanguageInfo(
            "PHP",
            "PH",
            "PHP is a scripting language built for server-side web development.",
            "Used in dynamic websites, CMS platforms, and backend web logic.",
            new Color(110, 125, 202)
        ),
        new LanguageInfo(
            "TypeScript",
            "TS",
            "TypeScript extends JavaScript with static typing.",
            "Used in scalable frontend and backend JavaScript projects.",
            new Color(39, 119, 201)
        ),
        new LanguageInfo(
            "Ruby",
            "RB",
            "Ruby is a dynamic language focused on developer happiness.",
            "Used in web development, especially with the Ruby on Rails framework.",
            new Color(197, 52, 80)
        ),
        new LanguageInfo(
            "Dart",
            "DT",
            "Dart is optimized for client-side UI development.",
            "Used in Flutter for mobile, web, and desktop cross-platform apps.",
            new Color(34, 140, 222)
        ),
        new LanguageInfo(
            "R",
            "R",
            "R is a statistical computing language for analysis and data visuals.",
            "Used in data science, research, and analytics reporting.",
            new Color(84, 118, 185)
        )
    );

    private DeckFactory() {
    }

    public static List<CardModel> buildDeck(GameSettings.Difficulty difficulty) {
        return buildDeck(difficulty.getRows(), difficulty.getColumns());
    }

    public static List<CardModel> buildDeck(int rows, int columns) {
        int pairCount = (rows * columns) / 2;
        if (pairCount > LANGUAGE_LIBRARY.size()) {
            throw new IllegalArgumentException("Not enough languages to build the selected grid size.");
        }

        List<LanguageInfo> pool = new ArrayList<>(LANGUAGE_LIBRARY);
        Collections.shuffle(pool, RANDOM);
        List<LanguageInfo> selected = new ArrayList<>(pool.subList(0, pairCount));

        List<CardModel> deck = new ArrayList<>(pairCount * 2);
        int id = 1;
        for (LanguageInfo language : selected) {
            deck.add(new CardModel(id++, language));
            deck.add(new CardModel(id++, language));
        }

        Collections.shuffle(deck, RANDOM);
        return deck;
    }
}
