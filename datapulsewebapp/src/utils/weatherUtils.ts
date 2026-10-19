const WEATHER_DESCRIPTIONS: Record<number, string> = {
  0: "Ciel dégagé",
  1: "Principalement dégagé",
  2: "Partiellement nuageux",
  3: "Ciel couvert",

  45: "Brouillard",
  48: "Brouillard givrant",

  51: "Bruine légère",
  53: "Bruine modérée",
  55: "Bruine dense",
  56: "Bruine verglaçante légère",
  57: "Bruine verglaçante dense",

  61: "Pluie légère",
  63: "Pluie modérée",
  65: "Pluie forte",
  66: "Pluie verglaçante légère",
  67: "Pluie verglaçante forte",

  71: "Chutes de neige légères",
  73: "Chutes de neige modérées",
  75: "Chutes de neige fortes",
  77: "Neige en grains",

  80: "Averses de pluie légères",
  81: "Averses de pluie modérées",
  82: "Averses de pluie violentes",
  85: "Averses de neige légères",
  86: "Averses de neige fortes",

  95: "Orage",
  96: "Orage avec grêle légère",
  97: "Orage violent",
  99: "Orage avec forte grêle",
};

export function getWeatherDescription(
  code: number | null | undefined
): string {
  if (code == null) {
    return "Conditions non disponibles";
  }

  return WEATHER_DESCRIPTIONS[code] ?? "Conditions inconnues";
}