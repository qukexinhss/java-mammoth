package org.zwobble.mammoth.styles;

import org.zwobble.mammoth.documents.NumberingLevel;
import org.zwobble.mammoth.documents.Paragraph;

import java.util.Optional;

public class ParagraphMatcher implements DocumentElementMatcher<Paragraph> {
    public static final ParagraphMatcher ANY = new ParagraphMatcher(Optional.empty(), Optional.empty(), Optional.empty());

    public static ParagraphMatcher styleId(String styleId) {
        return new ParagraphMatcher(Optional.of(styleId), Optional.empty(), Optional.empty());
    }

    public static ParagraphMatcher styleName(String styleName) {
        return new ParagraphMatcher(Optional.empty(), Optional.of(styleName), Optional.empty());
    }

    public static ParagraphMatcher orderedList(String level) {
        return new ParagraphMatcher(Optional.empty(), Optional.empty(), Optional.of(NumberingLevel.ordered(level)));
    }

    public static ParagraphMatcher unorderedList(String level) {
        return new ParagraphMatcher(Optional.empty(), Optional.empty(), Optional.of(NumberingLevel.unordered(level)));
    }

    private final Optional<String> styleId;
    private final Optional<String> styleName;
    private final Optional<NumberingLevel> numbering;

    public ParagraphMatcher(Optional<String> styleId, Optional<String> styleName, Optional<NumberingLevel> numbering) {
        this.styleId = styleId;
        this.styleName = styleName;
        this.numbering = numbering;
    }

    @Override
    public boolean matches(Paragraph paragraph) {
        return matchesStyle(paragraph) && matchesNumbering(paragraph);
    }

    private boolean matchesStyle(Paragraph paragraph) {
        return DocumentElementMatching.matchesStyle(styleId, styleName, paragraph.getStyle());
    }

    private boolean matchesNumbering(Paragraph paragraph) {
        return DocumentElementMatching.matches(numbering, paragraph.getNumbering(), (first, second) ->
            first.isOrdered() == second.isOrdered() && first.getLevelIndex().equalsIgnoreCase(second.getLevelIndex()));
    }
}