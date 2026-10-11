import { ValidationChain } from 'express-validator';

// One policy for every way a password is set (own change, admin create/reset)
export function passwordRules(chain: ValidationChain): ValidationChain {
  return chain
    .isLength({ min: 8 })
    .withMessage('Passwort muss mindestens 8 Zeichen haben')
    .matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/)
    .withMessage('Passwort muss Groß-/Kleinbuchstaben und eine Zahl enthalten');
}
