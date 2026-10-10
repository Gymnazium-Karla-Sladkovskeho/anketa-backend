const messages = {
	cs: {
		'app.title': 'Školní ankety',
		'landing.start': 'Začít',
		'login.login': 'Přihlašovací jméno',
		'login.password': 'Heslo',
		'login.submit': 'Přihlásit se',
		'login.empty': 'Vyplňte jméno i heslo.',
		'login.failed': 'Jméno nebo heslo nesedí. Zkuste to znovu.'
	},
	en: {
		'app.title': 'School Surveys',
		'landing.start': "Let's Start",
		'login.login': 'Your login',
		'login.password': 'Your password',
		'login.submit': 'Log in',
		'login.empty': 'Enter both login and password.',
		'login.failed': 'Wrong login or password. Try again.'
	}
} as const;

export type Locale = keyof typeof messages;
export type MessageKey = keyof (typeof messages)['cs'];

export const i18n = $state<{ locale: Locale }>({ locale: 'cs' });
export const t = (key: MessageKey): string => messages[i18n.locale][key];
