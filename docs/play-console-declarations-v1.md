# VibeCheck V1 — Play Console declaration audit

This document is preparation material. Final Play Console answers must be checked against the exact signed artifact uploaded to Google Play.

## Data and accounts

Current app design does not require a VibeCheck account. Player names, local groups, preferences and local statistics are intended to remain on-device.

Before submission, inspect the final dependency/permission report and confirm whether any SDK transmits diagnostics, billing, device or other data off-device. Do not declare "no data collected" solely from application-domain code without checking the final artifact and Google Play SDK disclosures.

## Billing

V1 is ad-free. The legacy Google Play Billing integration remains in the codebase for future use, but the V1 release contract disables the ad-removal purchase:

- `ReleaseConfig.adRemovalPurchaseEnabled == false`;
- the Premium / ad-removal card is not shown;
- the billing connection is not started during normal V1 startup;
- no active `remove_ads_lifetime` Play product is required for V1.

If monetization is reintroduced later, repeat the billing, privacy, Data Safety and store-copy review before publishing that update.

## Ads declaration

Determine from the exact V1 artifact whether advertising SDKs or user-visible ads are present. The Play Console Ads declaration must match the shipped artifact. If ads are introduced after this audit, re-check Data Safety and consent/privacy requirements.

## Solo simulations

Public-persona answers in Solo Party are fictional entertainment simulations. Store listing, in-app setup, gameplay, result screen and share copy must continue to state this clearly. They must not be described as statements, endorsements, opinions or actual participation by the referenced people.

## App access

No VibeCheck account is required for the core app. If no external authentication gate is introduced before release, reviewers should not need credentials to reach the core experience.

## Content rating / audience

Complete the questionnaire from the actual question catalog and final release configuration. Review question packs for mature themes before choosing target audience or age suitability. Do not infer the rating from the visual style.

## Privacy policy

Whether Play requires a privacy-policy URL depends on the final declarations, SDK behavior, permissions, audience and distribution configuration. If a policy is supplied, it must describe the exact shipped behavior, including Google Play Billing and any later analytics/ads SDKs.

## Final artifact gate

Before production:
- inspect merged manifest permissions for release;
- inspect final dependency tree / SDKs;
- compare Data Safety answers with actual transmitted data;
- compare Ads declaration with actual UI and SDKs;
- verify billing product/copy;
- complete content rating and target audience accurately;
- use screenshots captured from the exact release candidate or Play-delivered build.
