import type { Weather } from "./weather";

export interface CityWeatherState {
  weather: Weather | null;
  isLoading: boolean;
  error: string | null;
}