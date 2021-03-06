package org.zwobble.mammoth.internal.docx;

import java.util.List;
import java.util.Map;

import static org.zwobble.mammoth.internal.util.Lists.list;
import static org.zwobble.mammoth.internal.util.Maps.*;

public class Relationships {
    public static final Relationships EMPTY = new Relationships(list());

    private final Map<String, String> targetsByRelationshipId;
    private final Map<String, List<String>> targetsByType;

    public Relationships(List<Relationship> relationships) {
        this.targetsByRelationshipId = toMap(relationships, relationship -> entry(
            relationship.getRelationshipId(),
            relationship.getTarget()
        ));
        this.targetsByType = toMultiMap(relationships, relationship -> entry(
            relationship.getType(),
            relationship.getTarget()
        ));
    }

    public String findTargetByRelationshipId(String relationshipId) {
        return lookup(targetsByRelationshipId, relationshipId)
            .orElseThrow(() -> new RuntimeException("Could not find relationship '" + relationshipId + "'"));
    }

    public List<String> findTargetsByType(String type) {
        return lookup(targetsByType, type).orElse(list());
    }
}
