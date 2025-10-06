import React from "react";
import { userApi } from "./api/userApi";

const LoginContext = React.createContext({});

export function AppContextProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [jwt, setJwt] = React.useState("init");
  const [userInfo, setUserInfo] = React.useState<UserInfo>({
    username: "",
    nickname: "",
  });
  const [message, setMessage] = React.useState("");

  React.useEffect(() => {
    if (!jwt.trim() || jwt == "init") refresh();
  }, []);

  const refresh = async (): Promise<string> => {
    try {
      const jw = await userApi.refreshToken();
      setJwt(jw.jwt);
      return jw.jwt;
    } catch (error) {
      setJwt("");
      return "";
    }
  };

  React.useEffect(() => {
    if (jwt.trim())
      userApi
        .userinfo(jwt)
        .then(setUserInfo)
        .catch((err) => {
          const error = err as ErrorDto | "refresh";
          if (error == "refresh") {
            refresh();
          }
        });
  }, [jwt]);

  const login = (data: LoginDto) => {
    userApi
      .login(data)
      .then((d) => {
        console.log(d);
        setJwt(d.jwt);
        setMessage("");
      })
      .catch((err) => {
        setMessage((err as Error).message);
      });
  };
  const logout = async () => {
    try {
      await userApi.logout();
    } catch (error) {}
    setJwt("");
    setUserInfo({ username: "", nickname: "" });
  };
  return (
    <LoginContext.Provider
      value={{ jwt, userInfo, login, logout, message, setMessage, refresh }}
    >
      {children}
    </LoginContext.Provider>
  );
}

export const UseAppContext = () =>
  React.useContext(LoginContext) as {
    jwt: string;
    login(data: LoginDto): void;
    logout(): void;
    setMessage(a: string): void;
    message: string;
    refresh(): Promise<string>;
    userInfo: UserInfo;
  };
