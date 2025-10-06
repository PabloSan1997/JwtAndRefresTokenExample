import { propsApi } from "./propsApi";

export const taskApi = {
  async findAll(jwt: string): Promise<Tarea[]> {
    const ft = await fetch(`${propsApi.urlbase}/tarea`, {
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
  async deleteById(jwt: string, id: number): Promise<{ id: number }> {
    const ft = await fetch(`${propsApi.urlbase}/tarea/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    if (!ft.ok) {
      const error = (await ft.json()) as ErrorDto;
      if (ft.status == 401 && error.message == "expiration") throw "refresh";
      throw new Error(error.message);
    }
    return { id };
  },
  async save(jwt: string, data: TareaTitle): Promise<Tarea> {
    const ft = await fetch(`${propsApi.urlbase}/tarea`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        "Content-type": "application/json",
      },
      body: JSON.stringify(data),
    });
    if (!ft.ok) {
      const error = (await ft.json()) as ErrorDto;
      if (ft.status == 401 && error.message == "expiration") throw "refresh";
      throw new Error(error.message);
    }
    return ft.json();
  },
  async updateById(jwt: string, id: number, data: TareaDto): Promise<Tarea> {
    const ft = await fetch(`${propsApi.urlbase}/tarea/${id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        "Content-type": "application/json",
      },
      body: JSON.stringify(data),
    });
    if (!ft.ok) {
      const error = (await ft.json()) as ErrorDto;
      if (ft.status == 401 && error.message == "expiration") throw "refresh";
      throw new Error(error.message);
    }
    return ft.json();
  },
};
