// Pfad: src/client/java/com/partymann2000/interfaces/IApiStatusUpdater.java
package com.partymann2000.interfaces;

import com.partymann2000.PterodactylApiHelper;
import java.util.Optional;

public interface IApiStatusUpdater {
    // Dies ist der "Vertrag": Jede Klasse, die dieses Interface implementiert,
    // MUSS diese Methode haben.
    void xenority_updateApiStatus(Optional<PterodactylApiHelper.PteroServerInfo> infoOptional);
}