const btn = document.querySelector('button');
const paragraph = document.querySelector('p');
const resultSpan = document.querySelector('span');
const inputField = document.querySelector('input')
const characters = (input: number | string) => {
  if (typeof input === "number") return input;
  return input.length
}
btn.onclick = () => {
  const num = inputField.value;
  const resultingAge = characters(num)
  resultSpan.innerText = resultingAge.toString();
  paragraph.removeAttribute("hidden")
}

type test = {
  id: number,
  name: string,
  salary: number,
  department?: string,
}

const theBees = (word: string[]) => {
  return word.filter(x => x.startsWith('b'));
}
