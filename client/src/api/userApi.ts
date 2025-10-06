import { propsApi } from "./propsApi";

export const userApi = {
  async login(data: LoginDto): Promise<TokenDto> {
    const ft = await fetch(`${propsApi.urlbase}/user/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(data),
    });
    if (!ft.ok) throw new Error(((await ft.json()) as ErrorDto).message);
    return ft.json();
  },
  async userinfo(jwt: string): Promise<UserInfo> {
    const ft = await fetch(`${propsApi.urlbase}/user/userinfo`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    if (!ft.ok) {
      const error = (await ft.json()) as ErrorDto;
      if (ft.status == 401 && error.message == "expiration") throw "refresh";
      throw new Error(error.message);
    }
    return ft.json();
  },
  async refreshToken(): Promise<{ jwt: string }> {
    const ft = await fetch(`${propsApi.urlbase}/user/refreshauth`, {
      method: "POST",
      credentials: "include",
    });
    if (!ft.ok) throw new Error("Inicie seccion de nuevo");
    return ft.json();
  },
  async logout(): Promise<void> {
    const ft = await fetch(`${propsApi.urlbase}/user/logout`, {
      method: "POST",
      credentials: "include",
    });
    if (!ft.ok) {
      const error = (await ft.json()) as ErrorDto;
      if (ft.status == 401 && error.message == "expiration") throw "refresh";
      throw new Error(error.message);
    }
  },
};
