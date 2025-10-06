interface LoginDto {
  username: string;
  password: string;
}

interface UserInfo{
    username:string;
    nickname:string;
}

interface TokenDto {
  jwt: string;
}

interface ErrorDto {
  message: string;
  statusCode: number;
  error: string;
}

interface Tarea {
  id: number;
  title: string;
  state: boolean;
  createdAt: string;
  updatedAt: string;
}

interface TareaDto{
    title:string;
	state:boolean;
}
interface TareaTitle{
    title:string;
}