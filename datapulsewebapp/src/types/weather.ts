export interface Weather {
  time: string | null;
  timezone: string | null;
  temperature: number | null;
  apparentTemperature: number | null;
  humidity: number | null;
  windSpeed: number | null;
  weatherCode: number | null;
}