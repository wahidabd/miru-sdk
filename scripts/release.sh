#!/usr/bin/env bash
# Usage:
#   ./scripts/release.sh            → bump patch (0.1.5 → 0.1.6)
#   ./scripts/release.sh minor      → bump minor (0.1.5 → 0.2.0)
#   ./scripts/release.sh major      → bump major (0.1.5 → 1.0.0)
#   ./scripts/release.sh 1.2.3      → set explicit version

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
BUILD_FILE="$ROOT_DIR/build.gradle.kts"

# Read current version
CURRENT=$(grep -E '^\s*version\s*=\s*"[0-9]+\.[0-9]+\.[0-9]+"' "$BUILD_FILE" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')
if [[ -z "$CURRENT" ]]; then
  echo "ERROR: Could not read version from $BUILD_FILE" >&2
  exit 1
fi

MAJOR=$(echo "$CURRENT" | cut -d. -f1)
MINOR=$(echo "$CURRENT" | cut -d. -f2)
PATCH=$(echo "$CURRENT" | cut -d. -f3)

BUMP="${1:-patch}"

case "$BUMP" in
  major)
    NEW_VERSION="$((MAJOR + 1)).0.0"
    ;;
  minor)
    NEW_VERSION="${MAJOR}.$((MINOR + 1)).0"
    ;;
  patch)
    NEW_VERSION="${MAJOR}.${MINOR}.$((PATCH + 1))"
    ;;
  [0-9]*.[0-9]*.[0-9]*)
    NEW_VERSION="$BUMP"
    ;;
  *)
    echo "Usage: $0 [patch|minor|major|x.y.z]" >&2
    exit 1
    ;;
esac

echo "Bumping: $CURRENT → $NEW_VERSION"

# Update build.gradle.kts
sed -i '' "s/version = \"$CURRENT\"/version = \"$NEW_VERSION\"/" "$BUILD_FILE"

# Commit + tag
cd "$ROOT_DIR"
git add build.gradle.kts
git commit -m "chore: release $NEW_VERSION"
git tag "v$NEW_VERSION"

echo ""
echo "✓ Committed and tagged v$NEW_VERSION"
echo ""

# Confirm before push + publish
read -rp "Push ke remote dan publish ke Maven Central? (y/N) " CONFIRM
if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
  echo "Dibatalkan. Jalankan manual jika mau:"
  echo "  git push && git push origin v$NEW_VERSION"
  echo "  ./gradlew publishAllPublicationsToMavenCentralRepository"
  exit 0
fi

echo ""
echo "→ Pushing..."
git push
git push origin "v$NEW_VERSION"

echo ""
echo "→ Publishing to Maven Central..."
./gradlew publishAllPublicationsToMavenCentralRepository

echo ""
echo "✓ Release v$NEW_VERSION selesai!"
