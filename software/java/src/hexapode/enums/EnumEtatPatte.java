package hexapode.enums;

/**
 * Ne pas modifier l'ordre de l'enum; ça a une signification sémantique.
 * @author pf
 *
 */

public enum EnumEtatPatte {
    ARRIERE,
    AVANT,
	POUSSE,
	DEBOUT, // = levé en avant
	HAUT,
	POSE,
	OTHER; // Other: position manuelle, désasservi, ...

}
