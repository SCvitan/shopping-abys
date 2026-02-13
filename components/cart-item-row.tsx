"use client";

import { useState } from "react";
import { Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { removeFromCart, type CartItem } from "@/lib/api";
import { toast } from "sonner";
import { mutate } from "swr";

export function CartItemRow({ item }: { item: CartItem }) {
  const [removing, setRemoving] = useState(false);

  async function handleRemove() {
    setRemoving(true);
    try {
      await removeFromCart(item.productId);
      await mutate("cart");
      toast.success("Removed from cart");
    } catch {
      toast.error("Could not remove item");
    } finally {
      setRemoving(false);
    }
  }

  return (
    <div className="flex items-center justify-between border-b border-border py-4 last:border-b-0">
      <div className="flex flex-col gap-0.5">
        <span className="font-medium text-foreground">{item.title}</span>
        <span className="text-sm text-muted-foreground">
          {"Qty: "}{item.quantity} &middot; ${item.price.toFixed(2)} each
        </span>
      </div>
      <div className="flex items-center gap-4">
        <span className="font-semibold text-foreground">
          ${(item.price * item.quantity).toFixed(2)}
        </span>
        <Button
          variant="ghost"
          size="icon"
          onClick={handleRemove}
          disabled={removing}
          className="h-8 w-8 text-muted-foreground hover:text-destructive"
          aria-label={`Remove ${item.title} from cart`}
        >
          <Trash2 className="h-4 w-4" />
        </Button>
      </div>
    </div>
  );
}
