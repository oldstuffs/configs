package io.github.portlek.configs.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Replaceable {

    @NotNull
    private String text;

    @NotNull
    private final List<String> regex = new ArrayList<>();

    @NotNull
    private final List<Function<String, Response>> functions = new ArrayList<>();

    private Replaceable(@NotNull String text) {
        this.text = text;
    }

    @NotNull
    public Replaceable replace(@NotNull String regex, @NotNull Object object) {
        return replace(regex, () -> object);
    }

    @NotNull
    public Replaceable replace(@NotNull String regex, @NotNull Supplier<Object> object) {
        text = text.replace(regex, String.valueOf(object.get()));

        return this;
    }

    @NotNull
    public Replaceable replace(@NotNull Function<String, Response> functions) {
        return replace(Collections.singletonList(functions));
    }

    @NotNull
    public Replaceable replace(@NotNull List<Function<String, Response>> functions) {
        this.functions.addAll(functions);

        return this;
    }

    @NotNull
    public Replaceable replaces(@NotNull String... regex) {
        return replaces(Arrays.asList(regex));
    }

    @NotNull
    public Replaceable replaces(@NotNull List<String> regex) {
        this.regex.addAll(regex);

        return this;
    }

    @NotNull
    public String build() {
        return text;
    }

    @NotNull
    public String build(@NotNull Function<String, Response> regexFunction) {
        String finalText = text;

        for (String r : this.regex) {
            final Optional<String> optionalString = regexFunction.apply(r).getResponse();

            if (optionalString.isPresent()) {
                finalText = finalText.replace(r, optionalString.get());
            }

            for (Function<String, Response> function : functions) {
                final Optional<String> functionOptional = function.apply(r).getResponse();

                if (functionOptional.isPresent()) {
                    finalText = finalText.replace(r, functionOptional.get());
                }
            }
        }

        return finalText;
    }

    @NotNull
    public List<String> getRegex() {
        return regex;
    }

    @NotNull
    public List<Function<String, Response>> getFunctions() {
        return functions;
    }

    @NotNull
    public static Replaceable of(@NotNull String text) {
        return new Replaceable(text);
    }

    public static class Response {

        @Nullable
        private final String optionalString;

        public Response(@Nullable String optionalString) {
            this.optionalString = optionalString;
        }

        @NotNull
        public static Response text(@NotNull String text) {
            return new Response(text);
        }

        @NotNull
        public static Response none() {
            return new Response(null);
        }

        @NotNull
        public Optional<String> getResponse() {
            return Optional.ofNullable(optionalString);
        }

    }

}
