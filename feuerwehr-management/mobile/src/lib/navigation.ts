import { router, type Href } from 'expo-router';

/** Back if there is a screen below (not the case after a deep link on web), otherwise `fallback`. */
export function goBackOr(fallback: Href) {
  if (router.canGoBack()) router.back();
  else router.replace(fallback);
}
