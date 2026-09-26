# VibeCheck — audit première version fonctionnelle

Date: 2026-09-25
Branche auditée: `feature/themes-solo-party`

## Objectif
Préparer une première version Android réellement testable rapidement sans casser la base stable, puis séparer ce qui est nécessaire pour un APK fonctionnel de ce qui est nécessaire uniquement pour une publication Play Store.

## État actuel

### Fonctionnel
- 4 modes multijoueur locaux.
- Parcours onboarding → accueil → groupe → partie → résultat.
- Mode Solo avec casting simulé, sélection 2 à 7 personnages, auto-composition et réponses déterministes hors ligne.
- 40 personas: 30 simulations publiques + 10 personnages originaux.
- Disclaimer explicite pour les personnalités publiques.
- 6 thèmes visuels persistants.
- Historique de questions, statistiques locales et classement multijoueur.
- Partage de résultat et challenges.
- Aucun compte requis.
- Aucun backend requis.
- Aucune permission sensible déclarée.
- CI: tests unitaires, compilation tests instrumentation, lint, APK debug et AAB release.

### Corrigé pendant l'audit
- Ajout d'une vraie icône launcher VibeCheck et branchement dans le manifeste.
- Icône dessinée avec palette navy / crème / corail / teal, sans esthétique néon générative.
- Données locales exclues de l'Android Auto Backup.
- Trafic HTTP non chiffré interdit.
- Offre Premium masquée tant que les publicités ne sont pas réellement présentes.
- Résultats Solo exclus des statistiques multijoueur et des challenges multijoueur.
- Partage Solo reformulé comme casting simulé.
- Onboarding mis à jour pour expliquer multijoueur + Solo.
- Auto-casting Solo renforcé pour garantir davantage de diversité.

## P0 — avant de qualifier l'APK de première version fonctionnelle
1. CI verte sur le dernier HEAD.
2. Installer le dernier APK debug sur un vrai appareil et faire un smoke test:
   - premier lancement / onboarding;
   - changement de thème + redémarrage;
   - partie multijoueur complète;
   - partie Solo complète;
   - quitter/reprendre/rejouer;
   - partage d'un résultat;
   - rotation / retour Android sans crash.
3. Corriger tout crash ou blocage observé.
4. Vérifier visuellement l'icône sur le launcher réel à petite taille.

## P0 Play Store — uniquement pour publication publique
1. Signer l'AAB avec une upload key. La CI génère actuellement un AAB non signé.
2. Créer la fiche Play Console et activer Play App Signing.
3. Préparer politique de confidentialité / Data Safety cohérente avec le fonctionnement local.
4. Captures Play Store, feature graphic, description courte/longue.
5. Vérifier l'usage de noms de personnalités publiques et conserver les disclaimers; ne pas utiliser de photos non licenciées.

## P1 — qualité produit avant diffusion large
- Ajouter une vraie adaptive icon Android 8+ si le rendu des launchers masque mal le bitmap.
- Ajouter un écran À propos / Confidentialité dans Paramètres.
- Ajouter une action de suppression des données locales.
- Exécuter au moins un vrai test instrumentation sur émulateur/device dans une CI dédiée.
- Ajouter tests UI du parcours Solo complet jusqu'au résultat.
- Vérifier TalkBack, tailles de texte élevées et petits écrans.
- Ajuster les cartes de casting et l'écran résultat après test réel.

## P2 — après première version
- Publicités à des frontières naturelles de session.
- Réactiver ensuite l'achat `remove_ads_lifetime`.
- Analytics opt-in/minimal si réellement nécessaire.
- Plus de modes Solo.
- Adaptive sharing cards par thème.
- Assets supplémentaires et micro-animations uniquement si elles améliorent réellement la lisibilité.

## Décision release
Le produit est proche d'un APK MVP fonctionnel. Le principal blocage technique restant pour une installation de test est uniquement la validation CI + smoke test réel. Pour le Play Store public, la signature de l'AAB et les éléments de conformité/listing restent obligatoires.
