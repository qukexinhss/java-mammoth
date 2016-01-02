package org.zwobble.mammoth.docx;

import com.google.common.collect.ImmutableList;
import org.zwobble.mammoth.documents.*;
import org.zwobble.mammoth.xml.XmlElement;
import org.zwobble.mammoth.xml.XmlElementLike;
import org.zwobble.mammoth.xml.XmlNode;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

public class BodyXml {
    public static DocumentElement readBodyXmlElement(XmlElement element) {
        return readElement(element).get(0);
    }

    private static List<DocumentElement> readElement(XmlElement element) {
        switch (element.getName()) {
            case "w:t":
                return ImmutableList.of(new TextElement(element.innerText()));
            case "w:r":
                return ImmutableList.of(new RunElement(readElements(element.children())));
            case "w:p":
                return ImmutableList.of(readParagraph(element));

            case "w:pPr":
                return ImmutableList.of();

            default:
                throw new UnsupportedOperationException();
        }
    }

    private static ParagraphElement readParagraph(XmlElement element) {
        return new ParagraphElement(readParagraphStyle(element), readElements(element.children()));
    }

    private static Optional<Style> readParagraphStyle(XmlElement paragraph) {
        XmlElementLike properties = paragraph.findChildOrEmpty("w:pPr");
        return properties.findChildOrEmpty("w:pStyle").getAttributeOrNone("w:val")
            .map(styleId -> new Style(styleId, Optional.empty()));
    }

    private static List<DocumentElement> readElements(Iterable<XmlNode> nodes) {
        return ImmutableList.copyOf(
            concat(
                transform(
                    filter(nodes, XmlElement.class),
                    BodyXml::readElement)));
    }
}